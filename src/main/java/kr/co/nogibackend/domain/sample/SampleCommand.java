package kr.co.nogibackend.domain.sample;

public class SampleCommand {

	public record Create(
		String column1,
		String column2
	) {

	}

	public record Update(
		String column1,
		String column2
	) {

	}

}
