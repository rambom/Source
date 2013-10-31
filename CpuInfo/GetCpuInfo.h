#include <windows.h>
#include <string>
using namespace std;

extern "C" __declspec(dllexport) string _cdecl GetCpuSerialNo();
