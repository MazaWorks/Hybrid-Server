/**
 *  HybridServer
 *  Copyright (C) 2018 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Comprueba que dos cadenas de texto son iguales ignorando los espacios en blanco
 * y las mayúsculas y minúsculas.
 */
public class EqualsToIgnoringSpacesAndCaseMatcher extends BaseMatcher<String> {
	private final String expected;
	
	public EqualsToIgnoringSpacesAndCaseMatcher(String expected) {
		this.expected = expected;
	}
	
	@Override
	public boolean matches(Object item) {
		if (item instanceof String) {
			final String text = (String) item;
			
			return expected.replaceAll("\\s", "").equalsIgnoreCase(text.replaceAll("\\s", ""));
		}
		
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Strings are not equal ignoring whitespaces and case");
	}
	
	public static EqualsToIgnoringSpacesAndCaseMatcher equalsToIgnoringSpacesAndCase(String expected) {
		return new EqualsToIgnoringSpacesAndCaseMatcher(expected);
	}
}
