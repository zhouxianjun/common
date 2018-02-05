package com.alone.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 17-6-26 下午12:53
 */
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1000846308236576343L;

    @Getter
    @Setter
    private int pageNum;

    @Getter
    @Setter
    private int pageSize;
    @Getter
    private int count;

    @Setter
    private List<T> items;

    @Setter
    @Getter
    private Map<String, String> query;

    public void setCount(int count) {
        this.count = count;
    }
    public List<T> getItems() {
        if(items == null) {
            items = new ArrayList<>();
        }
        return items;
    }
    public int getTotalPage() {
        int totalPage = count / pageSize;
        if (totalPage == 0 || count % pageSize != 0) {
            totalPage++;
        }
        return totalPage;
    }

    /**
     * 获取当前记录索引
     * @return
     */
    public int getCurrentIndex() {
        int currentIndex = (getPageNum() - 1) * getPageSize();
        if (currentIndex < 0) {
            currentIndex = 0;
        }
        return currentIndex;
    }
}