package kr.co.nogibackend.config.logging.cachebody;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.util.StreamUtils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

	private byte[] cachedBody;

	public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		InputStream requestInputStream = request.getInputStream();
		this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
	}

	@Override
	public ServletInputStream getInputStream() {
		return new CachedBodyServletInputStream(this.cachedBody);
	}

	@Override
	public BufferedReader getReader() {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
		return new BufferedReader(new InputStreamReader(byteArrayInputStream));
	}

	private static class CachedBodyServletInputStream extends ServletInputStream {

		private final InputStream cachedBodyInputStream;

		public CachedBodyServletInputStream(byte[] cachedBody) {
			this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
		}

		@Override
		public boolean isFinished() {
			try {
				return cachedBodyInputStream.available() == 0;
			} catch (IOException e) {
				throw new RuntimeException("[CachedBodyServletInputStream] cachedBodyInputStream available fail", e);
			}
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int read() throws IOException {
			return cachedBodyInputStream.read();
		}
	}
}