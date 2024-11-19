package pt.ipleiria.estg.dei.dae.academics.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.stream.Collectors;

public class ForbiddenException extends Exception {
  public ForbiddenException(ConstraintViolationException e) {
    super(getConstraintViolationMessages(e));
  }
  private static String
  getConstraintViolationMessages(ConstraintViolationException e) {
    return e.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining("; "));
  }
}
