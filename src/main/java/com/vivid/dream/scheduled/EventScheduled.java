package com.vivid.dream.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduled {
//    @Resource(name = "eventRedisTemplate")
//    private StringRedisTemplate redisTemplate;
//    private final EventRepository eventRepository;
//
//    @Scheduled(fixedDelay = 2000)
//    public void eventSongStatusModify() {
//        boolean lock = false;
//        try {
//            lock = RedisLockUtils.tryLock(redisTemplate, RedisContent.EVENT_SONG_LOCK, 10000);
//            if (!lock) {
//                return;
//            }
//            LocalDateTime nowDate = LocalDateTime.now();
//            List<Event> eventList = eventRepository.findAll();
//            for (Event event : eventList) {
//                redisTemplate.opsForHash().putIfAbsent("Event-" + event.getId(), event.getId() + "_Status_", event.getStatus());
//            }
//        } catch (Exception e) {
//            log.warn("eventSongStatusModify error {}", e.getMessage(), e);
//        } finally {
//            if (lock) {
//                RedisLockUtils.releaseLock(redisTemplate, RedisContent.EVENT_SONG_LOCK);
//            }
//        }
//    }
}
