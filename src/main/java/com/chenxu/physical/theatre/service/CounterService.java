package com.chenxu.physical.theatre.service;

import com.chenxu.physical.theatre.model.Counter;

import java.util.Optional;

public interface CounterService {

  Optional<Counter> getCounter(Integer id);

  void upsertCount(Counter counter);

  void clearCount(Integer id);
}
