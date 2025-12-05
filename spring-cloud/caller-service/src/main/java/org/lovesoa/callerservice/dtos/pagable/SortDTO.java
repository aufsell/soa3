package org.lovesoa.callerservice.dtos.pagable;

import lombok.Data;

@Data
public class SortDTO {
    private boolean sorted;
    private boolean unsorted;
    private boolean empty;
}
