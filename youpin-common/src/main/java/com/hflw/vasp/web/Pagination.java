package com.hflw.vasp.web;

public class Pagination {
    private int pageStart = 1;
    private int currentPage = 1;
    private int pageSize = 10;

    public Pagination() {
    }

    public int getPageStart() {
        return this.pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
