package code.sibyl.util;

import code.sibyl.common.r;
import code.sibyl.controller.IndexController;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *  日期循环执行类
 */
@Data
public class DateLoopRunner {

    private String from;
    private String to;
    private Function<Date, Date> dateFunction;
    private String parsePattern;
    private boolean parallel = false;

    public static DateLoopRunner getInstance() {
        return new DateLoopRunner();
    }

    public DateLoopRunner from(String from) {
        this.setFrom(from);
        return this;
    }

    public DateLoopRunner function(Function<Date, Date> f) {
        this.setDateFunction(f);
        return this;
    }

    public DateLoopRunner parsePattern(String parsePattern) {
        this.setParsePattern(parsePattern);
        return this;
    }

    public DateLoopRunner parallel() {
        this.parallel = true;
        return this;
    }

    public DateLoopRunner to(String to) {
        this.setTo(to);
        return this;
    }

    public void run(Consumer<String> consumer) {
        Date date = r.parseDate(from, this.parsePattern);
        Date now = StringUtils.isNotBlank(to) ? r.parseDate(to, this.parsePattern) : new Date();
        if (this.parallel) {
            ArrayList<String> dateList = new ArrayList<>();
            while (now.equals(date) || now.after(date)) {
                String targetDate = DateFormatUtils.format(date, this.parsePattern);
                dateList.add(targetDate);
                date = this.dateFunction.apply(date);
            }
            dateList.stream().parallel().forEach(consumer::accept);

        } else {
            String targetDate;
            while (now.equals(date) || now.after(date)) {
                targetDate = DateFormatUtils.format(date, this.parsePattern);
                consumer.accept(targetDate);
                date = this.dateFunction.apply(date);
            }
        }
    }

    public static void main123(String[] args) {
        DateLoopRunner
                .getInstance()
                .from("2022-01")
                .to("2022-12")
                .parsePattern("yyyy-MM")
                .function(d -> DateUtils.addMonths(d, 1))
                .run(s -> System.err.println(s))
        ;
    }

}
