package org.lovesoa.calledadapter.soap.model;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String v) throws Exception {
        if (v == null || v.isEmpty()) return null;
        return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        if (v == null) return null;
        return v.toString();
    }
}
