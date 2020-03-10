pandoc ../overview.md -f markdown -t latex --toc -o ./export/release-notes-stories-exercises.pdf
pandoc ../kbs.txt -f markdown -t latex --toc -o ./export/knownbugs.pdf
pandoc -s ../overview.md ../kbs.txt -f markdown -t latex --toc -o ./export/overview-kbs.pdf
pandoc -s ../notes.md ../overview.md ../kbs.txt -f markdown -t latex --toc -o ./export/instructornotes.pdf