/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.tasks

import org.gradle.integtests.fixtures.AbstractIntegrationSpec

class ExecutionTimeTaskConfigurationIntegrationTest extends AbstractIntegrationSpec {

    def "throws decent exception when action is added to a task at execution time"() {
        when:
        buildFile.text = """
            task foo1 << {
                doLast { println 'foo1' }
            }

            task foo2 << {
                doFirst { println 'foo2' }
            }
        """
        then:
        args("--continue")
        fails("foo1", "foo2")
        and:
        failure.assertHasCause("You cannot add a task action at execution time, please check the configuration of task task ':foo1'.")
        failure.assertHasCause("You cannot add a task action at execution time, please check the configuration of task task ':foo2'.")
    }

    def "throws decent warnings when task is configured during execution time"() {
        when:
        buildFile.text = """
            task bar

            task foo << {
                onlyIf{false}
                setActions(new ArrayList())
                dependsOn bar
                dependsOn = [bar]
                setOnlyIf({false})
                setOnlyIf({false})

                Spec spec = new Spec(){
                        public boolean isSatisfiedBy(Object element) {
                            return false;
                        }
                };
                setOnlyIf(spec)
                onlyIf(spec)
                enabled = false
                deleteAllActions()

                inputs.file("afile")
                inputs.files("anotherfile")
                inputs.dir("aDir")
                inputs.property("propertyName", "propertyValue")
                inputs.properties(["propertyName":"propertyValue"])
                inputs.source("aSource")
                inputs.sourceDir("aSourceDir")

                outputs.upToDateWhen{false}
                outputs.upToDateWhen(spec)
                outputs.file("afile")
                outputs.files("anotherfile")
                outputs.dir("aDir")
            }
        """
        and:
        executer.withDeprecationChecksDisabled()
        then:
        succeeds("foo")

        output.contains("Calling Task.onlyIf(Closure) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling Task.setActions(Actions<Task>) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling Task.dependsOn(Object...) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling Task.setDependsOn(Iterable) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling Task.setOnlyIf(Closure) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling Task.setOnlyIf(Spec) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling Task.onlyIf(Spec) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling Task.setEnabled(boolean) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling Task.deleteAllActions() after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskInputs.dir(Object) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskInputs.files(Object...) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskInputs.file(Object) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskInputs.property(String, Object) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskInputs.properties(Map) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskInputs.source(Object) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskInputs.sourceDir(Object) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskOutputs.upToDateWhen(Closure) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskOutputs.upToDateWhen(Spec) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskOutputs.file(Object) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskOutputs.files(Object...) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
        output.contains("Calling TaskOutputs.dir(Object) after task execution has started has been deprecated and is scheduled to be removed in Gradle 2.0 Check the configuration of task task ':foo'. You may have misused '<<' at task declaration.")
    }
}