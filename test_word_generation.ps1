# 测试Word文档生成功能
Write-Host "测试Word文档生成功能..."

# 测试简单Word生成
Write-Host "1. 测试简单Word生成..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/simple-proof/1" -UseBasicParsing
    Write-Host "简单Word生成状态: $($response.StatusCode)"
    if ($response.StatusCode -eq 200) {
        Write-Host "简单Word生成成功，大小: $($response.Content.Length) bytes"
        # 保存Word文件
        $response.Content | Set-Content -Path "test_simple_proof.docx" -Encoding Byte
        Write-Host "简单Word文档已保存为 test_simple_proof.docx"
    }
} catch {
    Write-Host "简单Word生成错误: $($_.Exception.Message)"
}

# 测试通过查询生成Word
Write-Host "2. 测试通过查询生成Word..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/proof/query?name=张星雨&idCardNumber=110101199001011234" -UseBasicParsing
    Write-Host "查询Word生成状态: $($response.StatusCode)"
    if ($response.StatusCode -eq 200) {
        Write-Host "查询Word生成成功，大小: $($response.Content.Length) bytes"
        # 保存Word文件
        $response.Content | Set-Content -Path "test_query_proof.docx" -Encoding Byte
        Write-Host "查询Word文档已保存为 test_query_proof.docx"
    }
} catch {
    Write-Host "查询Word生成错误: $($_.Exception.Message)"
}

# 测试通过ID生成Word
Write-Host "3. 测试通过ID生成Word..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/proof/1" -UseBasicParsing
    Write-Host "ID Word生成状态: $($response.StatusCode)"
    if ($response.StatusCode -eq 200) {
        Write-Host "ID Word生成成功，大小: $($response.Content.Length) bytes"
        # 保存Word文件
        $response.Content | Set-Content -Path "test_id_proof.docx" -Encoding Byte
        Write-Host "ID Word文档已保存为 test_id_proof.docx"
    }
} catch {
    Write-Host "ID Word生成错误: $($_.Exception.Message)"
}

Write-Host "测试完成！"
Write-Host "现在可以通过浏览器访问 http://localhost:8080 进行完整测试："
Write-Host "1. 访问查询页面"
Write-Host "2. 输入姓名：张星雨，身份证：110101199001011234"
Write-Host "3. 点击'导出证明'按钮下载Word文档"




