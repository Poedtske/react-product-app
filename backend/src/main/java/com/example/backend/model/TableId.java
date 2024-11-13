package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
class TableId implements Serializable {
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "table_id")
    private Long tableId;

    // Constructors
    public TableId() {}

    public TableId(Long eventId, Long tableId) {
        this.eventId = eventId;
        this.tableId = tableId;
    }

    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    // Override equals and hashCode for composite key comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableId tableId = (TableId) o;
        return Objects.equals(eventId, tableId.eventId) &&
                Objects.equals(this.tableId, tableId.tableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, tableId);
    }
}
