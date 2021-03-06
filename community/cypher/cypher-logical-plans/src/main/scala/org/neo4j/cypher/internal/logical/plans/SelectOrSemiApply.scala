/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.logical.plans

import org.neo4j.cypher.internal.expressions.Expression
import org.neo4j.cypher.internal.util.attribution.IdGen

/**
  * Like SemiApply, but with a precondition 'expr'. If 'expr' is true, left row will be produced without
  * executing right.
  *
  * for ( leftRow <- left ) {
  *   if ( leftRow.evaluate( expr) ) {
  *     produce leftRow
  *   } else {
  *     right.setArgument( leftRow )
  *     if ( right.nonEmpty ) {
  *       produce leftRow
  *     }
  *   }
  * }
  */
case class SelectOrSemiApply(left: LogicalPlan, right: LogicalPlan, expr: Expression)(implicit idGen: IdGen)
  extends AbstractSelectOrSemiApply(left, right, expr)(idGen)

/**
  * Like AntiSemiApply, but with a precondition 'expr'. If 'expr' is true, left row will be produced without
  * executing right.
  *
  * for ( leftRow <- left ) {
  *   if ( leftRow.evaluate( expr) ) {
  *     produce leftRow
  *   } else {
  *     right.setArgument( leftRow )
  *     if ( right.isEmpty ) {
  *       produce leftRow
  *     }
  *   }
  * }
  */
case class SelectOrAntiSemiApply(left: LogicalPlan, right: LogicalPlan, expr: Expression)(implicit idGen: IdGen)
  extends AbstractSelectOrSemiApply(left, right, expr)(idGen)

abstract class AbstractSelectOrSemiApply(left: LogicalPlan, right: LogicalPlan, expr: Expression)(idGen: IdGen)
  extends LogicalPlan(idGen) with ApplyPlan {
  val lhs = Some(left)
  val rhs = Some(right)

  val availableSymbols: Set[String] = left.availableSymbols
}
