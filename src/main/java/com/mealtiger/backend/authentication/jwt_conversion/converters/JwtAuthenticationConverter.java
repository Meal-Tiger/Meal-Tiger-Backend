package com.mealtiger.backend.authentication.jwt_conversion.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtAuthenticationConverter extends Converter<Jwt, AbstractAuthenticationToken> {
}
