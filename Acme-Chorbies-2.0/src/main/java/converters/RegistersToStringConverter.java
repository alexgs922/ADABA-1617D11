
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Registers;

@Component
@Transactional
public class RegistersToStringConverter implements Converter<Registers, String> {

	@Override
	public String convert(final Registers source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
