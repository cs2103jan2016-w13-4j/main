# User Guide

## Installation

### Pre-requisites

* JDK 1.8 or higher is required
* A computer with a graphic display and a keyboard


## Layout
![Mockup](http://i.imgur.com/C4PO6mI.jpg)

## Features

### Quick-start tutorial
A overlayed tutorial is shown upon the first startup of the
application. It introduces to the user the layout of the application
and some basic commands to help the user get familiarized with the
application quickly.

### Deadline reminders
JFDI can send desktop notifications when deadlines are
approaching. The exact timing of the alert can be configured by the
user. This is handy as it reduces the chance of the user missing the deadlines.

### Wildcard task
JFDI can suggest arbitary tasks from the task list for the user when
he or she is absolutely free and bored.

### Sub-checklists
JFDI allows the user to create a sub-checklist for a task. This makes
it easier to manage tasks that are done incrementally.

### Custom aliases
JDFI supports creating custom aliases for commands. This allows the
user to create custom shortcuts for frequently used commands so as to
further improve productivity. It is also helpful when the user finds
some commands hard to remember, they can just bind them to another
easier-to-remember command.


## Usage

### Commands

##### Adding tasks

* Adding a task
  ```
  add <do something> (on | at | by) <datetime>
  ```

  `<datetime>` can be either a date, a time or a combination of them. If only time is specified without a date, task will be create at that specific time within the following 24 hours. Date can be date strings like `08/02/2016`, `04 Jan`, `Sunday`, or even abbreviations like `Sun`.
  If the pre-position is `by`, then the task will be added as a *deadline*, otherwise it will be added as a normal task.
  <br>

* Adding an event
  ```lisp
  add <event name> from <starting time> to <end time>
  ```
  <br>

* Create a recurring task
  ```lisp
  add <do something> every <datetime> [until <datetime>]
  ```

  The `until` part is optional. When omitted, task will be repeated forever.
  <br>

* Marking a task as done
  ```lisp
  mark <#task_id> as done
  ```

  Each task is associated with a unique `task_id`, which is usually
  displayed beside the task name for easy reference.
  <br>

* Change priority of task
  ```lisp
  mark <#task_id> as urgent
  ```

  There are three types of priorities, `urgent`, `normal` and `low`.
  <br>

* Deleting a task
  ```lisp
  delete <#task_id>
  ```

  <br>

* Rescheduleing an event
  ```lisp
  reschedule <#task_id> to <datetime>
  ```

  <br>

* Set an alert for a task
  ```lisp
  remind me <time> before <#task_id>
  ```

  By default, a task created with reminder will have its reminder cleverly set.
  - If the task is scheduled on a date, then the reminder will be set on one day before the date.
  - If the task is scheduled on a time specific to hour, then the reminder will be set to one hour before the time.
  - If the task is shceduled on a time specific to minute, then the reminder will be set to 15 mins before the time.
  <br>


* Search for tasks
  ```lisp
  search <keyword>
  ```

  If there is no uppercase letters in the `<keyboard>`, the search is case-insensitive. Otherwise it is case-sensitive.
  <br>


* Wildcard task
  ```lisp
  surprise me
  ```

  This selects a random task from all the uncompleted tasks. Tasks that has higher priorities have higher probabilities of being picked.
  <br>

* define custom aliases
  ```lisp
  define "<new_command>" to "<original_command>"
  ```

  Both the original and old command have to be quoted because either of them might contain keywords like "to".
  <br>

* Show help
  ```lisp
  [help | tutorial]
  ```

  `help` shows user manual, whereas `tutorial` replays the quick-start tutorial.
  <br>


### Keyboard shortcuts

* Edit last command: <kbd>&uarr;</kbd>
* Scroll through previous commands: <kbd>&uarr;</kbd> or <kbd>&darr;</kbd>

## Cheatsheet (TODO)
