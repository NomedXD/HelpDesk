package com.innowise.dto.response;

public record FileInfoResponse (
        String name,
        Long size,
        String type){
}