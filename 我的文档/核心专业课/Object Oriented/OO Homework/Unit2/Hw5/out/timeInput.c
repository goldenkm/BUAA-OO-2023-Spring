// timeInput.c
#include <stdio.h>

#ifdef WIN32
#include <windows.h>
#define SLEEP_MS(x) Sleep((x))
#else
#include <unistd.h>
#define SLEEP_MS(x) usleep(1000*(x))
#endif

char buf[1005];
long curMillis;
const int maxLen = 100;

int main()
{
    long millis;
    double sec;
    long pointsec;
    FILE *fp1;
    fp1=fopen("stdin.txt", "r");
    while (fscanf(fp1, "[%lf]", &sec) != EOF) {
        millis = sec * 1000;
        fscanf(fp1, "%s\n", buf);
        SLEEP_MS(millis - curMillis);
        puts(buf);
        fflush(stdout);
        curMillis = millis;
    }
    return 0;
}