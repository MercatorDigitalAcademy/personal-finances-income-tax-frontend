package models

class Error extends Exception {}
case object CannotSaveChildError extends Error
