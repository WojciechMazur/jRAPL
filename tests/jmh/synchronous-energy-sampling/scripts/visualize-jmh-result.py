#!/usr/bin/env python3

import os
import json
from sys import argv

import matplotlib.pyplot as plt
from matplotlib.ticker import FormatStrFormatter

def plt_set_axis_limits(xrange, yrange, xaxis_precision, yaxis_precision):
    none = (None,None)
    if ((xrange)!=(none)):
        plt.xlim(xrange)
        if xaxis_precision != 0:
            plt.gca().xaxis.set_major_formatter(FormatStrFormatter('%.'+str(xaxis_precision)+'f'))
        else:
            plt.xticks(range(xrange[0],xrange[1]+1))
    if ((yrange)!=(none)):
        plt.ylim(yrange)
        if yaxis_precision != 0:
            plt.gca().yaxis.set_major_formatter(FormatStrFormatter('%.'+str(yaxis_precision)+'f'))
        else:
            plt.yticks(range(yrange[0],yrange[1]+1))

# if len(argv) != 2:
try:
    jmh_result = argv[1]
    result_dir = argv[2]
except:
    print('usage:',argv[0],'jmh-result.json result_dir')
    exit(2)

with open(jmh_result) as f:
    _data = json.loads(f.read())

data = {}
for d in _data:
    name = d['benchmark'].split('.')[-1]
    data[name] = d

unit    =  [];
means   =  [];
labels  =  [];
errors  =  []; # not standard deviation. not sure what the error is

for name in data:
    if name.startswith('time'):
        labels.append(name.replace('time','',1))
    else:
        labels.append(name)
    means.append( data[name]['primaryMetric']['score'] )
    errors.append( data[name]['primaryMetric']['scoreError'] )
    unit.append( data[name]['primaryMetric']['scoreUnit'] )

assert(sorted(unit) == sorted(unit)[-1::-1]) # idiom for 'all items equal'
unit = unit[0]

print(labels)
print(means)
print(errors)
print('units:',unit)

xrange = (None, None)
yrange = (32, 36)
xaxis_precision, yaxis_precision = (0, 0)
plt_set_axis_limits(xrange, yrange, xaxis_precision, yaxis_precision)

plt.bar(
    range(len(labels)),
    means,
    yerr = errors,
    tick_label = labels,
    color = 'cadetblue',
    edgecolor = 'black',
    alpha = 1
);

plt.ylabel('Average Runtime ({})'.format(unit))
plt.xlabel('Sampling Version')
# plt.title('average sample runtime'.title())
plt.savefig(os.path.join(result_dir,'sync-samples-runtime'))
