package com.vam.model;

import  java.util.Date;

public class ReplyDTO {

    private int replyId;        // 댓글 id

    private int bookId;         // 상품 id

    private String memberId;    // 회원 id

    private Date reDate;        // 등록일

    private String content;     // 내용

    private double rating;      // 별점

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Date getReDate() {
        return reDate;
    }

    public void setReDate(Date reDate) {
        this.reDate = reDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override 
    public String toString() { // toString 디버깅할때나 값을 출력해서 보고 싶을 때 편리하게 사용가능
        return "ReplyDTO [bookId=" + bookId + ", content=" + content + ", memberId=" + memberId + ", rating=" + rating
                + ", reDate=" + reDate + ", replyId=" + replyId + "]";
    }
    
    
}
