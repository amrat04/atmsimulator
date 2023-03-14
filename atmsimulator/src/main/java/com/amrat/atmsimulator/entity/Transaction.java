package com.amrat.atmsimulator.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Transaction {

    private final String from;
    private final String to;
    private final BigDecimal amount;

    @NotNull
    public String getStatement() {
        return new StringBuilder()
                .append("Transferred $")
                .append(amount.setScale(0))
                .append(" to ")
                .append(to)
                .toString();
    }
}
