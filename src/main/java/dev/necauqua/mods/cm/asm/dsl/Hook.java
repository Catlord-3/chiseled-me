/*
 * Copyright (c) 2016-2019 Anton Bulakh <necauqua@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.necauqua.mods.cm.asm.dsl;

import java.util.function.Consumer;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface Hook extends Consumer<ContextMethodVisitor> {

    default Hook and(Hook other) {
        return mv -> {
            accept(mv);
            other.accept(mv);
        };
    }

    default Hook at(int pos) {
        return mv -> {
            if (mv.getPass() == pos) {
                accept(mv);
            }
        };
    }

    default Hook filter(IntPredicate pred) {
        return mv -> {
            if (pred.test(mv.getPass())) {
                accept(mv);
            }
        };
    }
}
