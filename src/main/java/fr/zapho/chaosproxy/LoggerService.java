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
package fr.zapho.chaosproxy;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoggerService {
    List<Log> logs = new CopyOnWriteArrayList<>();

    public List<String> getLogs(String levelStr) {
        Optional<LogLevel> levelOpt = LogLevel.from(levelStr);
        Predicate<Log> logsWithProperRank = (Log log) -> levelOpt.map(l -> log.getLevel().getRank() >= l.getRank()).orElse(true);

        return logs.stream()
                .filter(logsWithProperRank)
                .map(l -> l.message())
                .collect(Collectors.toList());
    }

    public void log(LogLevel level, Logger logger, String msg, Object... args) {
        if (level == null || msg == null || msg.isEmpty() || !shouldLog(level, logger)) {
            return;
        }
        String formattedMsg = MessageFormatter.format(msg, args).getMessage();
        logs.add(new Log(level, formattedMsg));
        switch (level) {
            case TRACE:
                logger.trace(formattedMsg);
                break;
            case DEBUG:
                logger.debug(formattedMsg);
                break;
            case INFO:
                logger.info(formattedMsg);
                break;
            case WARN:
                logger.warn(formattedMsg);
                break;
            case ERROR:
                logger.error(formattedMsg);
                break;
            default:
                logger.error(formattedMsg);
                break;
        }
    }

    public void cleanLogs() {
        logs.clear();
    }

    private boolean shouldLog(LogLevel level, Logger logger) {
        switch (level) {
            case TRACE:
                return logger.isTraceEnabled();
            case DEBUG:
                return logger.isDebugEnabled();
            case INFO:
                return logger.isInfoEnabled();
            case WARN:
                return logger.isWarnEnabled();
            case ERROR:
                return logger.isErrorEnabled();
            default:
                return false;
        }
    }
}
