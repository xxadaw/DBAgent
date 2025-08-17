package com.dbagent.instance.domain;

import lombok.*;

/**
 * @Classname RequestInstance
 * @Description TODO
 * @Date 2025/8/10 16:54
 * @Created by xxx
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInstance extends Instance{
    protected Integer offset;
    protected Integer limit;

    public String toString() {
        return "RequestInstance{" +
                "id=" + id + '\'' +
                ", userId=" + userId + '\'' +
                "instanceId='" + instanceId + '\'' +
                ", instanceType='" + instanceType + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", databaseName='" + databaseName + '\'' +
                ", username='" + username + '\'' +
                ", passwordEnc='" + passwordEnc + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", status=" + status +
                ", offset=" + offset +
                ", limit=" + limit +
                ", text='" + text + '\'' +
                '}';
    }
}
