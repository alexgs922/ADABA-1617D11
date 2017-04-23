
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.OrganisesRepository;
import domain.Organises;

@Component
@Transactional
public class StringToOrganisesConverter implements Converter<String, Organises> {

	@Autowired
	OrganisesRepository	organisesRepository;


	@Override
	public Organises convert(final String text) {
		Organises result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.organisesRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
