package org.puregxl.site.infra.convention;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessage {

    public enum Role {
        /**
         * 系统角色，一般用于设定对话规则、身份设定、风格约束等
         */
        SYSTEM,

        /**
         * 用户角色，表示真实用户的提问或输入内容
         */
        USER,

        /**
         * 助手机器人角色，表示大模型返回的回复内容
         */
        ASSISTANT;

        /**
         * 根据字符串值匹配对应的角色枚举
         *
         * @param value 角色字符串值，不区分大小写
         * @return 匹配到的 {@link Role} 枚举值
         * @throws IllegalArgumentException 当传入的字符串无法匹配任何角色时抛出异常
         */
        public static Role fromString(String value) {
            for (Role role : Role.values()) {
                if (role.name().equalsIgnoreCase(value)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("无效的角色类型: " + value);
        }
    }


    private Role role;

    private String content;

    public ChatMessage(Role role, String content) {
        this.role = role;
        this.content = content;
    }


    /**
     * SYSTEM
     * @param prompt
     * @return
     */
    public static ChatMessage system(String prompt) {
        return new ChatMessage(Role.SYSTEM, prompt);
    }


    /**
     * USER
     * @param prompt
     * @return
     */
    public static ChatMessage user(String prompt) {
        return new ChatMessage(Role.USER, prompt);
    }


    /**
     * ASSISTANT
     * @param prompt
     * @return
     */
    public static ChatMessage assistant(String prompt) {
        return new ChatMessage(Role.ASSISTANT, prompt);
    }


}
