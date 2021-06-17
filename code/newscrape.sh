killall -9 lynx
i=55450
step=50
while [ $i -lt 120000 ]; do
    echo "$i"
    for j in $(seq 1 1 $step)
        do
        k=$((i+j))
        urlid="$(sed "${k}q;d" urlsX)"
        id="$(echo $urlid | cut -d',' -f1)"
        url="$(echo $urlid | cut -d',' -f2)"
        torify lynx -width=10000 -connect_timeout=100 -image_links -dump $url > "pages2/$id" &
        sleep 1
    done
    wait < <(jobs -p)
    sudo service tor restart
    sleep 2
    let i=$((i+step))
done

