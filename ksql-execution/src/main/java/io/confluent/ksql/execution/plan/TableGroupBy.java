/*
 * Copyright 2019 Confluent Inc.
 *
 * Licensed under the Confluent Community License; you may not use this file
 * except in compliance with the License.  You may obtain a copy of the License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.execution.plan;

import com.google.errorprone.annotations.Immutable;
import io.confluent.ksql.execution.expression.tree.Expression;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Immutable
public class TableGroupBy<K> implements ExecutionStep<KGroupedTableHolder> {
  private final ExecutionStepProperties properties;
  private final ExecutionStep<KTableHolder<K>> source;
  private final Formats formats;
  private final List<Expression> groupByExpressions;

  public TableGroupBy(
      final ExecutionStepProperties properties,
      final ExecutionStep<KTableHolder<K>> source,
      final Formats formats,
      final List<Expression> groupByExpressions
  ) {
    this.properties = Objects.requireNonNull(properties, "properties");
    this.source = Objects.requireNonNull(source, "source");
    this.formats = Objects.requireNonNull(formats, "formats");
    this.groupByExpressions = Objects.requireNonNull(groupByExpressions, "groupByExpressions");
  }

  @Override
  public ExecutionStepProperties getProperties() {
    return properties;
  }

  @Override
  public List<ExecutionStep<?>> getSources() {
    return Collections.singletonList(source);
  }

  public Formats getFormats() {
    return formats;
  }

  public List<Expression> getGroupByExpressions() {
    return groupByExpressions;
  }

  public ExecutionStep<KTableHolder<K>> getSource() {
    return source;
  }

  @Override
  public KGroupedTableHolder build(final PlanBuilder builder) {
    return builder.visitTableGroupBy(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TableGroupBy<?> that = (TableGroupBy<?>) o;
    return Objects.equals(properties, that.properties)
        && Objects.equals(source, that.source)
        && Objects.equals(formats, that.formats)
        && Objects.equals(groupByExpressions, that.groupByExpressions);
  }

  @Override
  public int hashCode() {

    return Objects.hash(properties, source, formats, groupByExpressions);
  }
}
