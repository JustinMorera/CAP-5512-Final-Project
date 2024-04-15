import tkinter as tk
from tkinter import ttk
import subprocess
import os
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

 
def plot_graph(frame):
    for widget in frame.winfo_children():
        widget.destroy()

    df = pd.read_csv('fitnessdata.csv', header=None, names=['Generation', 'Average Fit', 'Best Fit', 'Population'])

    def get_dividing_number(column):
        max_abs_value = df[column].abs().max()
        if max_abs_value > 10000:
            return 1000
        elif max_abs_value > 1000:
            return 100
        elif max_abs_value > 100:
            return 10
        else:
            return 1

    dividing_number_best_fit = get_dividing_number('Best Fit')
    dividing_number_average_fit = get_dividing_number('Average Fit')
    dividing_number_population = get_dividing_number('Population')

    fig, ax = plt.subplots()
    ax.plot(df['Generation'], df['Best Fit'] / dividing_number_best_fit, 
            label=f'Best Fit (divided by {dividing_number_best_fit})', marker='o')
    ax.plot(df['Generation'], df['Average Fit'] / dividing_number_average_fit, 
            label=f'Average Fit (divided by {dividing_number_average_fit})', marker='o')
    ax.plot(df['Generation'], df['Population'] / dividing_number_population, 
            label=f'Population (divided by {dividing_number_population})', marker='o')
    
    ax.set_xlabel('Generation')
    ax.set_ylabel('Fitness and Population')
    ax.set_title('Fitness Evolution Over Generations')

    ax.legend()

    canvas = FigureCanvasTkAgg(fig, master=frame)
    canvas.draw()
    canvas.get_tk_widget().pack(side=tk.TOP, fill=tk.BOTH, expand=1)


def parse_params_file(filepath):
    params = {}
    try:
        with open(filepath, 'r') as file:
            keys = ["Exp ID:", "Prob Type:", "Input File:", "Num Of Runs:", "Gens Per Run:", "Pop Size:", "Selection:",
              "Fit Scale Type:", "Xover Type:", "Xover Rate:", "Mutate Type:", "Mutate Rate:",
              "Random Seed:", "Fecundity:", "Fitness Threshold:"]
            for i, line in enumerate(file):
                if i < 14:
                    value = line[30:].strip()
                    if i < len(keys):
                        params[keys[i]] = value
                else:
                    break
    except FileNotFoundError:
        print("No parameters file found.")
    return params
    
def save_params(entries):
    params_data = f"""\
Experiment ID                :{entries['Exp ID:'].get()}
Problem Type                 :{entries['Prob Type:'].get()}
Data Input File Name         :{entries['Input File:'].get()}
Number of Runs               :{entries['Num Of Runs:'].get()}
Generations per Run          :{entries['Gens Per Run:'].get()}
Population Size              :{entries['Pop Size:'].get()}
Selection Method (1)         :{entries['Selection:'].get()}
Fitness Scaling Type (2)     :{entries['Fit Scale Type:'].get()}
Crossover Type (3)           :{entries['Xover Type:'].get()}
Crossover Rate (4)           :{entries['Xover Rate:'].get()}
Mutation Type (5)            :{entries['Mutate Type:'].get()}
Mutation Rate (6)            :{entries['Mutate Rate:'].get()}
Random Number Seed           :{entries['Random Seed:'].get()}
Fecundity: (9)               :{entries['Fecundity:'].get()}
Fitness Threshold            :{entries['Fitness Threshold:'].get()}

Notes:

1)  Selection Type Codes    1 = Proportional Selection
                            2 = Tournament Selection
                            3 = Random Selection

2)  Fitness Scaling Type    0 = Scale for Maximization (no change to raw fitness)
                            1 = Scale for Minimization (reciprocal of raw fitness)
                            2 = Rank for Maximization
                            3 = Rank for Minimization

3)  Crossover Type Codes    1 = Single Point Crossover
                            2 = Two Point Crossover
                            3 = Uniform Crossover

4)  Crossover Rates from 0 to 1, Use "0" to turn off crossover

5)  Mutation Type Codes     1 = Flip Bit

6)  Mutation Rates from 0 to 1, Use "0" to turn off mutation

7)  Represents number of genes in each chromosome.

8)  Determines number of bits in each gene.  Number of Genes times Size
    gives the number of bits in each chromosome.

9)  Number of offspring per mating event (each offspring undergoes separate crossover)
"""
    with open("GUIPARAMS.GUI", "w") as file:
        file.write(params_data)
        
     
def populate_entries(entries, params):
    for key, entry in entries.items():
        entry.delete(0, tk.END)
        if key in params:
            entry.insert(0, params[key])

     
     
running_process = None
        
def terminate_process():
    global running_process
    if running_process:
        running_process.terminate()
        running_process = None

def run_ga(terminal):
    global running_process
    terminate_process() 

    terminal.delete(1.0, tk.END)
    terminal.insert(tk.END, "Starting GA process...\n")

    try:
        running_process = subprocess.Popen("javac *.java", shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        stdout, stderr = running_process.communicate()
        if stdout:
            terminal.insert(tk.END, "Compilation Output:\n" + stdout)
            terminal.see(tk.END)
        if stderr:
            terminal.insert(tk.END, "Compilation Errors:\n" + stderr)
            terminal.see(tk.END)

        if running_process.returncode == 0:
            running_process = subprocess.Popen(["java", "-Xmx4096M", "Search", "GUIPARAMS.GUI"], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
            stdout, stderr = running_process.communicate()
            if stdout:
                terminal.insert(tk.END, "Execution Output:\n" + stdout)
                terminal.see(tk.END)
            if stderr:
                terminal.insert(tk.END, "Execution Errors:\n" + stderr)
                terminal.see(tk.END)
        else:
            terminal.insert(tk.END, "Compilation failed. Java program will not run.\n")
            terminal.see(tk.END)
    except Exception as e:
        terminal.insert(tk.END, f"An error occurred: {e}\n")
        terminal.see(tk.END)


def main():
    root = tk.Tk()
    root.title("AdaptiCritters v3.4.7")
    root.geometry("1170x930+0+0")
    root.maxsize(1170, 930)

    frame_params = ttk.Frame(root, borderwidth=3, cursor="arrow")
    frame_params.place(width=300, height=770, x=850, y=30)

    frame_graph = ttk.Frame(root)
    frame_graph.place(width=800, height=550, x=30, y=30)

    labels = ["Input File:", "Num Of Runs:", "Gens Per Run:", "Pop Size:", "Selection:",
              "Fit Scale Type:", "Xover Type:", "Xover Rate:", "Mutate Type:", "Mutate Rate:",
              "Random Seed:", "Fecundity:", "Fitness Threshold:", "Exp ID:", "Prob Type:"]
    entries = {}

    for i, label_text in enumerate(labels):
        label = ttk.Label(frame_params, text=label_text + ":", font=("Noto Sans", 8))
        label.place(relwidth=0.5, relheight=0.05, rely=i*0.05)

        entry = ttk.Entry(frame_params, font=("Noto Sans", 8), cursor="xterm")
        entry.place(relx=0.5, relwidth=0.45, relheight=0.05, rely=i*0.05)
        entries[label_text] = entry

    params = parse_params_file("GUIPARAMS.GUI")
    populate_entries(entries, params)

    params_button = ttk.Button(frame_params, text="Create Params File", command=lambda: save_params(entries))
    params_button.place(rely=0.75, relwidth=0.95, relheight=0.05)

    frame_ga = ttk.Frame(root, borderwidth=3, cursor="arrow")
    frame_ga.place(width=800, height=288, x=30, y=610)

    terminal = tk.Text(frame_ga, font=("Noto Sans", 6), foreground="#ffffff", background="#000000")
    terminal.place(x=0, y=0, relwidth=1.0, height=192)

    ga_button = ttk.Button(frame_ga, text="Run GA", command=lambda: run_ga(terminal))
    ga_button.place(x=0, y=192, relwidth=1.0, height=48)
    
    graph_button = ttk.Button(frame_ga, text="Make Graph", command=lambda: plot_graph(frame_graph))
    graph_button.place(x=0, y=240, relwidth=1.0, height=48)

    root.mainloop()

if __name__ == "__main__":
    main()
