
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Organises;

@Component
@Transactional
public class OrganisesToStringConverter implements Converter<Organises, String> {

	@Override
	public String convert(final Organises source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
