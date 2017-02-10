
package main;

public interface IMaze {
    void OnGenerateStart();
    void OnGenerateEnd();
    void OnSearchStart();
    void OnSearchEnd();
    void OnResetEnd();
}