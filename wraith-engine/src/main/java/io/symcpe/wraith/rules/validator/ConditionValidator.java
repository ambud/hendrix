/**
 * Copyright 2016 Symantec Corporation.
 * 
 * Licensed under the Apache License, Version 2.0 (the “License”); 
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.symcpe.wraith.rules.validator;

import java.util.ArrayList;
import java.util.List;

import io.symcpe.wraith.conditions.AbstractSimpleCondition;
import io.symcpe.wraith.conditions.Condition;
import io.symcpe.wraith.conditions.logical.ComplexCondition;
import io.symcpe.wraith.conditions.relational.EqualsCondition;
import io.symcpe.wraith.conditions.relational.NumericCondition;
import io.symcpe.wraith.rules.Rule;

/**
 * {@link Validator} for {@link Condition} associated with a {@link Rule}
 * 
 * @author ambud_sharma
 */
public class ConditionValidator implements Validator<Condition> {

	private List<Validator<Condition>> conditionValidators = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Override
	public void configure(List<Validator<?>> validators) {
		for (Validator<?> validator : validators) {
			try {
				conditionValidators.add((Validator<Condition>) validator);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void validate(Condition condition) throws ValidationException {
		if (condition instanceof ComplexCondition) {
			for (Condition childCondition : ((ComplexCondition) condition).getConditions()) {
				validate(childCondition);
			}
		} else {
			if (condition instanceof AbstractSimpleCondition) {
				AbstractSimpleCondition castedConditon = ((AbstractSimpleCondition) condition);
				if (castedConditon.getHeaderKey() == null || castedConditon.getHeaderKey().isEmpty()) {
					throw new ValidationException("Condition header key cannot be empty");
				}
				if (castedConditon instanceof NumericCondition) {
					if (((NumericCondition) castedConditon).getValue() == Double.MIN_VALUE) {
						throw new ValidationException("Numeric conditions must have a value");
					}
				}
				if (castedConditon instanceof EqualsCondition) {
					if (((EqualsCondition) castedConditon).getValue() == null) {
						throw new ValidationException("Equals condition must have a value");
					}
				}
			} else {
				// unsupported condition type
				throw new ValidationException("Unsupported condition");
			}
		}
		for (Validator<Condition> validator : conditionValidators) {
			validator.validate(condition);
		}
	}

}