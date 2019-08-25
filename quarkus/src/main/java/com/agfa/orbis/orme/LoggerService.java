/*
 *                  C O P Y R I G H T  (c) 2014
 *     A G F A   H E A L T H C A R E   C O R P O R A T I O N
 *                     All Rights Reserved
 *
 *
 *         THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
 *                       AGFA CORPORATION
 *        The copyright notice above does not evidence any
 *       actual or intended publication of such source code.
 */
package com.agfa.orbis.orme;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoggerService {
    List<Log> logs = new CopyOnWriteArrayList<>();

    public void log(LogLevel level, String msg) {
        if (level != null && msg != null && !msg.isEmpty()) {
            logs.add(new Log(level, msg));
        }
    }

    public List<String> getLogs(String levelStr) {
        Optional<LogLevel> levelOpt = LogLevel.from(levelStr);
        Predicate<Log> logsWithProperRank = (Log log) -> levelOpt.map(l -> log.getLevel().getRank() >= l.getRank()).orElse(true);

        return logs.stream()
                .filter(logsWithProperRank)
                .map(l -> l.message())
                .collect(Collectors.toList());
    }
}
