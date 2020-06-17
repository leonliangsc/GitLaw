# Data Processing
----
Spark batch processing built, packaged and shipped with sbt, written in Scala.

## How to run update:
 1. `python3 GitLaw/updator/update_script.py`
 2. or use Cron to schedule daily update:
  - start a Cronjob by typing the following command `crontab -e`
  - append the following command:
  ```bash
  # run python bills update script every 12 hours
  0 */12 * * * python3 ~/GitLaw/data-processing/updator/update_script.py
  ```
