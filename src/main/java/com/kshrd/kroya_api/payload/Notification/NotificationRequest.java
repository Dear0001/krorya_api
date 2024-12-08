package com.kshrd.kroya_api.payload.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String description;
    private Integer sender;
    private Integer receiver;
    private Long productId;
}
