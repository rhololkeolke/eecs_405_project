#!/usr/bin/env python
import numpy as np
import matplotlib.pyplot as plt
from matplotlib import cm
from mpl_toolkits.mplot3d import Axes3D
import os
import csv

if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Create plots of vgram merge skip data')
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
        for row in reader:
            qmin = int(row[1])
            qmax = int(row[2])
            raw_data_key = (qmin, qmax)
            raw_threshold_data = raw_data.get(raw_data_key, {})
            if len(raw_threshold_data) == 0:
                raw_data[raw_data_key] = raw_threshold_data

            threshold = int(row[3])
            k_data = raw_threshold_data.get(threshold, {})
            if len(k_data) == 0:
                raw_threshold_data[threshold] = k_data

            k = int(row[4])
            data_sets = k_data.get(k, {})
            if len(data_sets) == 0:
                k_data[k] = data_sets

            num_candidates = data_sets.get('num_candidates', [])
            if len(num_candidates) == 0:
                data_sets['num_candidates'] = num_candidates
            num_candidates.append(int(row[6]))

            candidate_times = data_sets.get('candidate_times', [])
            if len(candidate_times) == 0:
                data_sets['candidate_times'] = candidate_times
            candidate_times.append(int(row[7]))

            similar_strings = data_sets.get('similar_strings', [])
            if len(similar_strings) == 0:
                data_sets['similar_strings'] = similar_strings
            similar_strings.append(int(row[8]))

            refine_times = data_sets.get('refine_times', [])
            if len(refine_times) == 0:
                data_sets['refine_times'] = refine_times
            refine_times.append(int(row[9]))

    for threshold_data in raw_data.values():
        for k_data in threshold_data.values():
            for data_sets in k_data.values():
                data_sets['num_candidates'] = np.mean(data_sets['num_candidates'])
                data_sets['candidate_times'] = np.mean(data_sets['candidate_times'])
                data_sets['similar_strings'] = np.mean(data_sets['similar_strings'])
                data_sets['refine_times'] = np.mean(data_sets['refine_times'])

    # plot average candidates vs qmin vs qmax for each k and max threshold
    (qmins, qmaxes) = zip(*sorted(raw_data.keys()))
    avg_num_candidates = {}
    avg_candidate_times = {}
    avg_similar_strings = {}
    avg_refine_times = {}
    for (qmin, qmax) in zip(qmins, qmaxes):
        threshold = max(raw_data[(qmin, qmax)].keys())
        k_data = raw_data[(qmin, qmax)][threshold]

        for (k, data_sets) in k_data.items():
            data = avg_num_candidates.get(k, [])
            if len(data) == 0:
                avg_num_candidates[k] = data
            data.append(data_sets['num_candidates'])

            data = avg_candidate_times.get(k, [])
            if len(data) == 0:
                avg_candidate_times[k] = data
            data.append(data_sets['candidate_times']/10**6)

            data = avg_similar_strings.get(k, [])
            if len(data) == 0:
                avg_similar_strings[k] = data
            data.append(data_sets['similar_strings'])

            data = avg_refine_times.get(k, [])
            if len(data) == 0:
                avg_refine_times[k] = data
            data.append(data_sets['refine_times']/10**6)

    for (k, data) in avg_num_candidates.items():
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.plot_trisurf(qmins, qmaxes, data, cmap=cm.jet)
        ax.set_xlabel('qmin')
        ax.set_ylabel('qmax')
        ax.set_zlabel('Avg Number of Candidates')
        ax.set_title('Avg Number of Candidates vs Qmin vs Qmax\nFor k=%d\nFor VGram MergeSkip -- IMDB' % k)
        plt.savefig(os.path.join(args.output_dir, 'avg_num_candidates_vs_qmin_vs_qmax_k%d.png' % k))


    for (k, data) in avg_candidate_times.items():
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.plot_trisurf(qmins, qmaxes, data, cmap=cm.jet)
        ax.set_xlabel('qmin')
        ax.set_ylabel('qmax')
        ax.set_zlabel('Avg Candidate Search Time (ms)')
        ax.set_title('Avg Candidate Search Time vs Qmin vs Qmax\nFor k=%d\nFor VGram MergeSkip -- IMDB' % k)
        plt.savefig(os.path.join(args.output_dir, 'avg_candidate_times_vs_qmin_vs_qmax_k%d.png' % k))


    for (k, data) in avg_similar_strings.items():
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.plot_trisurf(qmins, qmaxes, data, cmap=cm.jet)
        ax.set_xlabel('qmin')
        ax.set_ylabel('qmax')
        ax.set_zlabel('Avg Number Similar Strings')
        ax.set_title('Avg Number of Similar Strings vs Qmin vs Qmax\nFor k=%d\nFor VGram MergeSkip -- IMDB' % k)
        plt.savefig(os.path.join(args.output_dir, 'avg_similar_strings_vs_qmin_vs_qmax_k%d.png' % k))


    for (k, data) in avg_refine_times.items():
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.plot_trisurf(qmins, qmaxes, data, cmap=cm.jet)
        ax.set_xlabel('qmin')
        ax.set_ylabel('qmax')
        ax.set_zlabel('Avg Refine Time (ms)')
        ax.set_title('Avg Refine Time vs Qmin vs Qmax\nFor k=%d\nFor VGram MergeSkip -- IMDB' % k)
        plt.savefig(os.path.join(args.output_dir, 'avg_refine_times_vs_qmin_vs_qmax_k%d.png' % k))