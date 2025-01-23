package kr.co.nogibackend.domain.sample;

public class SampleInfo {
	public record Data(
		Long id,
		String column1,
		String column2
	) {
		public static SampleInfo.Data from(Sample sample) {
			return new SampleInfo.Data(
				sample.getId(),
				sample.getColumn1(),
				sample.getColumn2()
			);
		}
	}
}
