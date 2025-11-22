package org.lovesoa.calledejb.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}