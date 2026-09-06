package com.example.Areanixx.dto;

// generic response wrapper so every controller can return a consistent
// shape: { success, message, data } instead of raw entities/strings.
// optional to adopt - existing controllers work fine without it.
public class DtoFile<T> {

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	private boolean success;
	private String message;
	private T data;

	public DtoFile() {
	}

	public DtoFile(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public static <T> DtoFile<T> ok(String message, T data) {
		return new DtoFile<>(true, message, data);
	}

	public static <T> DtoFile<T> fail(String message) {
		return new DtoFile<>(false, message, null);
	}

}
