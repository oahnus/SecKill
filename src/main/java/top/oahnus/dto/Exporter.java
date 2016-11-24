package top.oahnus.dto;

/**
 * Created by oahnus on 2016/11/24.
 */
public class Exporter {
    private boolean exported;

    private String md5;

    private long seckillId;

    // 当前时间
    private long now;
    // 开启时间
    private long start;
    // 结束时间
    private long end;

    public Exporter(boolean exported, String md5, long seckillId) {
        this.exported = exported;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exporter(boolean exported, long now, long start, long end) {
        this.exported = exported;
        this.now = now;
        this.end = end;
        this.start = start;
    }

    public Exporter(boolean exported, long seckillId) {
        this.exported = exported;
        this.seckillId = seckillId;
    }

    public Exporter(boolean exported, long seckillId, long now, long start, long end) {
        this.exported = exported;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exporter{" +
                "exported=" + exported +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
