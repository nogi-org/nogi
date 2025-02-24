package kr.co.nogibackend.config.logging.cachebody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream cachedBody;

	private final ServletOutputStream outputStream;
	private final PrintWriter writer;

	public CachedBodyHttpServletResponse(HttpServletResponse response) throws IOException {
		super(response);
		this.cachedBody = new ByteArrayOutputStream();
		this.outputStream = new CachedBodyServletOutputStream(cachedBody);
		this.writer = new PrintWriter(new OutputStreamWriter(cachedBody, StandardCharsets.UTF_8));
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return outputStream;
	}

	@Override
	public PrintWriter getWriter() {
		return writer;
	}

	public byte[] getCachedBody() {
		return cachedBody.toByteArray();
	}

	public void copyBodyToResponse() throws IOException {
		byte[] body = getCachedBody();
		super.getOutputStream().write(body);
		super.getOutputStream().flush();
	}

	private static class CachedBodyServletOutputStream extends ServletOutputStream {

		private final ByteArrayOutputStream cachedBody;

		public CachedBodyServletOutputStream(ByteArrayOutputStream cachedBody) {
			this.cachedBody = cachedBody;
		}

		@Override
		public void write(int b) {
			cachedBody.write(b);
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			throw new UnsupportedOperationException();
		}
	}
}