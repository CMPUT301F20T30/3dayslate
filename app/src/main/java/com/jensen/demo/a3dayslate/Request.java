package com.jensen.demo.a3dayslate;

public class Request {
    private String owner;
    private String requester;
    private Book book;
    private Book.statuses status;
    private ExchangeLocation location;

    public Request(String requester, Book book) {
        this.book = book;
        this.owner = book.getOwner();
        this.requester = requester;
        this.status = book.getCurrentStatus();
    }

    public Request() {
        // Do NOT delete this
    }

    public String getOwner() {
        return owner;
    }

    public String getRequester() {
        return requester;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book.statuses getStatus() {
        return status;
    }

    public void setStatus(Book.statuses status) {
        this.status = status;
    }

    public ExchangeLocation getLocation() {
        return location;
    }

    public void setLocation(ExchangeLocation location) {
        this.location = location;
    }
}
