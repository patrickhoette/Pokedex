#!/usr/bin/env python3

import os

# Constants
GRADLE_HOME = f'{os.environ["HOME"]}/.gradle'
PROPERTIES_FILE = f'{GRADLE_HOME}/gradle.properties'

RAM = '10g'
METASPACE = '2g'

# Setup JVM args
print('Writing gradle properties...')

jvm_args_list = list()

jvm_args_list.append(f'-Xmx{RAM}')
jvm_args_list.append(f'-XX:MaxMetaspaceSize={METASPACE}')
jvm_args_list.append('-XX:+HeapDumpOnOutOfMemoryError')

jvm_args = f'org.gradle.jvmargs={" ".join(jvm_args_list)}'
print(f'Setting JVM args to: {jvm_args}')

# Setup gradle.properties
os.makedirs(GRADLE_HOME, exist_ok=True)
properties = open(PROPERTIES_FILE, 'w')

# Write properties
properties.write('\n' + jvm_args)
properties.write('\norg.gradle.parallel=true')
properties.write('\norg.gradle.daemon=false')
properties.write('\nkotlin.compiler.execution.strategy=in-process')

print('Successfully wrote gradle properties')

# Clean up
properties.close()
print('Setup script finished!')
