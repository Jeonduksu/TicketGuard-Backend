package com.ticketguard.domain.event.service;

import com.ticketguard.domain.event.entity.Event;
import com.ticketguard.domain.event.repository.EventRepository;
import com.ticketguard.domain.ticket.entity.Ticket;
import com.ticketguard.domain.ticket.repository.TicketRepository;
import com.ticketguard.domain.ticket.repository.TicketUseLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureApiService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final TicketUseLogRepository ticketUseLogRepository;
    private final WebClient webClient = WebClient.create();

    @Value("${culture.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://apis.data.go.kr/B553457/cultureinfo";
    private static final  DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public void fetchAndSaveEvents() {
        try{
            List<Event> allEvents = new ArrayList<>();

            // 분야별로 각각 호출
            String[] realmCodes = {"B000", "B001", "B002", "B003", "B004", "B005", "B006", "B007"};

            for (String realmCode : realmCodes) {
                String url = BASE_URL + "/realm2"
                        + "?serviceKey=" + apiKey
                        + "&realmCode=" + realmCode
                        + "&from=" + LocalDateTime.now().format(FORMATTER)
                        + "&to=" + LocalDateTime.now().plusMonths(3).format(FORMATTER)
                        + "&numOfRows=50"
                        + "&pageNo=1";

                try {
                    String response = webClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

                    List<Event> events = parseXmlToEvents(response);
                    allEvents.addAll(events);
                    log.info("[문화정보 API] realmCode={} {}개 조회", realmCode, events.size());
                } catch (Exception e) {
                    log.warn("[문화정보 API] realmCode={} 조회 실패", realmCode, e);
                }
            }

            ticketUseLogRepository.deleteAll();
            ticketRepository.deleteAll();
            eventRepository.deleteAll();
            eventRepository.saveAll(allEvents);

            log.info("[문화정보 API] 총 {}개 공연 저장 완료", allEvents.size());
        }catch (Exception e) {
            log.error("[문화정보 API] 데이터 가져오기 실패",e );
        }
    }

    private List<Event> parseXmlToEvents(String xml)  throws Exception {
        List<Event> events = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

        NodeList items = doc.getElementsByTagName("item");

        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);

            String title = getTagValue("title",item);
            String startDateStr = getTagValue("startDate",item);
            String endDateStr = getTagValue("endDate",item);
            String place = getTagValue("place",item);
            String thumbnail = getTagValue("thumbnail",item);
            String charge = getTagValue("charge",item);
            String realmName = getTagValue("realmName",item);

            if(title == null || startDateStr == null) continue;

            try {
                LocalDateTime startAt= LocalDate.parse(startDateStr, FORMATTER).atStartOfDay();
                LocalDateTime endAt= endDateStr != null
                        ? LocalDate.parse(endDateStr, FORMATTER).atTime(23,59,59)
                        : startAt.plusHours(3);

                int price = 0;
                if(charge != null && !charge.isEmpty() && !charge.equals("무료")) {
                    String numericCharge = charge.replaceAll("[^0-9]", "");
                    if(!numericCharge.isEmpty()) {
                        price = Integer.parseInt(numericCharge);
                    }
                }

                Event.EventCategory category = mapCategory(realmName);

                Event event = Event.builder()
                        .title(title)
                        .startsAt(startAt)
                        .endsAt(endAt)
                        .venue(place != null ? place : "미정")
                        .totalSeats(0)
                        .imageUrl(thumbnail)
                        .price(price)
                        .category(category)
                        .status(Event.EventStatus.PUBLISHED)
                        .build();

                events.add(event);
            }catch (Exception e) {
                log.warn("[파싱 오류] title = {}",title,e);
            }


        }

        return events;
    }

    private String getTagValue(String tag, Element element) {
        NodeList list = element.getElementsByTagName(tag);
        if(list.getLength() == 0) return null;
        Node node = list.item(0).getFirstChild();
        return node != null ? node.getNodeValue() : null;
    }

    private Event.EventCategory mapCategory(String realmName) {
        if(realmName == null) return Event.EventCategory.ETC;
        return switch (realmName) {
            case "음악/콘서트" -> Event.EventCategory.CONCERT;
            case "뮤지컬/오페라" -> Event.EventCategory.MUSICAL;
            case "연극" -> Event.EventCategory.THEATER;
            case "전시" -> Event.EventCategory.EXHIBITION;
            case "무용/발레" -> Event.EventCategory.CLASSIC;
            case "아동/가족" -> Event.EventCategory.FAMILY;
            case "행사/축제" -> Event.EventCategory.FESTIVAL;
            default -> Event.EventCategory.ETC;
        };
    }
}
