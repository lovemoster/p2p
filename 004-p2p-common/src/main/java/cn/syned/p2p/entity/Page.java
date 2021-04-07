package cn.syned.p2p.entity;

import java.io.Serializable;

/**
 * 分页对象模型
 */
public class Page implements Serializable {
    //当前页
    private Integer cunPage;
    //每页显示的记录数
    private Integer pageContent;
    //总的记录条数
    private Long totalCount;
    //总的页数
    private Integer totalPage;
    //第一页
    private Integer firstPage;
    //最后一页
    private Integer lastPage;

    public Page() {
    }

    public Page(Integer cunPage, Integer pageContent) {
        this.cunPage = cunPage;
        this.pageContent = pageContent;
    }

    public Integer getCunPage() {
        return cunPage;
    }

    public void setCunPage(Integer cunPage) {
        this.cunPage = cunPage;
    }

    public Integer getPageContent() {
        return pageContent;
    }

    public void setPageContent(Integer pageContent) {
        this.pageContent = pageContent;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Integer firstPage) {
        this.firstPage = firstPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }
}
