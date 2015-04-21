#!/usr/bin/env python
import numpy as np
import matplotlib.pyplot as plt
from matplotlib import cm
from mpl_toolkits.mplot3d import Axes3D
import os
import csv

if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Created plots of vgram creation data')
    parser.add_argument('data_file', type=str, help='Benchmark data file')
    parser.add_argument('output_dir', type=str, help='Output directory')

    args = parser.parse_args()

    if not os.path.exists(args.data_file):
        raise ValueError("%s does not exist" % args.data_file)

    if not os.path.exists(args.output_dir):
        os.makedirs(args.output_dir)

    raw_data = {}
    with open(args.data_file, 'r') as data_file:
        reader = csv.reader(data_file)
        next(reader)
        for row in reader:
            qmin = int(row[1])
            qmax = int(row[2])
            raw_data_key = (qmin, qmax)
            raw_threshold_data = raw_data.get(raw_data_key, {})
            if len(raw_threshold_data) == 0:
                raw_data[raw_data_key] = raw_threshold_data

            threshold = int(row[4])
            data_sets = raw_threshold_data.get(threshold, {})
            if len(data_sets) == 0:
                raw_threshold_data[threshold] = data_sets

            gram_dict_sizes = data_sets.get('gram_dict_sizes', [])
            if len(gram_dict_sizes) == 0:
                data_sets['gram_dict_sizes'] = gram_dict_sizes
            gram_dict_sizes.append(float(row[5]))

            inverted_list_sizes = data_sets.get('inverted_list_sizes', [])
            if len(inverted_list_sizes) == 0:
                data_sets['inverted_list_sizes'] = inverted_list_sizes
            inverted_list_sizes.append(float(row[6]))

            nag_sizes = data_sets.get('nag_sizes', [])
            if len(nag_sizes) == 0:
                data_sets['nag_sizes'] = nag_sizes
            nag_sizes.append(float(row[7]))

            total_sizes = data_sets.get('total_sizes', [])
            if len(total_sizes) == 0:
                data_sets['total_sizes'] = total_sizes
            total_sizes.append(float(row[8]))

            total_times = data_sets.get('total_times', [])
            if len(total_times) == 0:
                data_sets['total_times'] = total_times
            total_times.append(float(row[20]))

    gram_dict_sizes = []
    inverted_list_sizes = []
    nag_sizes = []
    total_sizes = []
    total_times = []
    (qmins, qmaxes) = zip(*sorted(raw_data.keys()))
    for (qmin, qmax) in zip(qmins, qmaxes):
        threshold = max(raw_data[(qmin, qmax)].keys())
        data_sets = raw_data[(qmin, qmax)][threshold]
            
        gram_dict_sizes.append(data_sets['gram_dict_sizes'][0])
        inverted_list_sizes.append(data_sets['inverted_list_sizes'][0])
        nag_sizes.append(data_sets['nag_sizes'][0])
        total_sizes.append(data_sets['total_sizes'][0])
        total_times.append(data_sets['total_times'][0])
            
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.plot_trisurf(qmins, qmaxes, gram_dict_sizes, cmap=cm.jet)
    ax.set_xlabel('qmin')
    ax.set_ylabel('qmax')
    ax.set_zlabel('Gram Dict Sizes (MB)')
    ax.set_title('Gram Dict Size vs Qmin vs QMax')
    plt.savefig(os.path.join(args.output_dir, 'gram_dict_size_vs_qmin_vs_qmax.png'))

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.plot_trisurf(qmins, qmaxes, inverted_list_sizes, cmap=cm.jet)
    ax.set_xlabel('qmin')
    ax.set_ylabel('qmax')
    ax.set_zlabel('Inverted Lists Sizes (MB)')
    ax.set_title('Inverted Lists Size vs Qmin vs QMax')
    plt.savefig(os.path.join(args.output_dir, 'inverted_list_size_vs_qmin_vs_qmax.png'))

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.plot_trisurf(qmins, qmaxes, nag_sizes, cmap=cm.jet)
    ax.set_xlabel('qmin')
    ax.set_ylabel('qmax')
    ax.set_zlabel('NAG Sizes (MB)')
    ax.set_title('NAG Size vs Qmin vs QMax')
    plt.savefig(os.path.join(args.output_dir, 'nag_size_vs_qmin_vs_qmax.png'))

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.plot_trisurf(qmins, qmaxes, total_sizes, cmap=cm.jet)
    ax.set_xlabel('qmin')
    ax.set_ylabel('qmax')
    ax.set_zlabel('Total Sizes (MB)')
    ax.set_title('Total Size vs Qmin vs QMax')
    plt.savefig(os.path.join(args.output_dir, 'total_size_vs_qmin_vs_qmax.png'))

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.plot_trisurf(qmins, qmaxes, total_times, cmap=cm.jet)
    ax.set_xlabel('qmin')
    ax.set_ylabel('qmax')
    ax.set_zlabel('Total Time (s)')
    ax.set_title('Total Time vs Qmin vs QMax')
    plt.savefig(os.path.join(args.output_dir, 'total_time_vs_qmin_vs_qmax.png'))
                
            
                
                
                