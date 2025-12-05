package org.lovesoa.callerservice.dtos.pagable;

import lombok.Data;

@Data
public class PageableDTO {
    private int pageNumber;
    private int pageSize;
    private SortDTO sort;
    private long offset;
    private boolean paged;
    private boolean unpaged;
}
