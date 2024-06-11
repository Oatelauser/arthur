package com.broadtech.arthur.admin.route.entity.po.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.unit.DataSize;

/**
 * @Author Machenike
 * @Date 2022/7/19
 * @Version 1.0.0
 * @Description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestHeaderSizeFilterPojo {
    private final String name = "RequestHeaderSize";
    private DataSize maxSize;


}
