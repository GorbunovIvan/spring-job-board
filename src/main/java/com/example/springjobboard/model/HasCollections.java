package com.example.springjobboard.model;

import java.util.Collection;
import java.util.Map;

public interface HasCollections {
    Map<String, Collection<? extends HasName>> getCollections();
}
