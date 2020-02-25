pandoc ../overview.md -f markdown -t latex --toc -o ./export/exercises.pdf
pandoc ../kbs.txt -f markdown -t latex --toc -o ./export/knownbugs.pdf
pandoc -s ../overview.md ../kbs.txt -f markdown -t latex --toc -o ./export/knownbugs.pdf