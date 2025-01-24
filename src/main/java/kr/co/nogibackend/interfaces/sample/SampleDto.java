package kr.co.nogibackend.interfaces.sample;

import kr.co.nogibackend.domain.sample.SampleCommand;
import kr.co.nogibackend.domain.sample.SampleInfo;

public class SampleDto {

	public record CreateRequest(
		String column1,
		String column2
	) {

		public SampleCommand.Create toCommand() {
			return new SampleCommand.Create(
				column1(),
				column2()
			);
		}
	}

	public record UpdateRequest(
		String column1,
		String column2
	) {

		public SampleCommand.Update toCommand() {
			return new SampleCommand.Update(
				column1(),
				column2()
			);
		}
	}

	public record Response(
		Long id,
		String column1,
		String column2
	) {

		public static SampleDto.Response from(SampleInfo.Data data) {
			return new SampleDto.Response(
				data.id(),
				data.column1(),
				data.column2()
			);
		}
	}

}
